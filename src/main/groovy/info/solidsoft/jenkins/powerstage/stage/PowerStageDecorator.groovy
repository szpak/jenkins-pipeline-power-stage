/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.solidsoft.jenkins.powerstage.stage

import java.util.concurrent.TimeUnit

/**
 * A stage decorator.
 *
 * It decorates stage with extra features within a stage context (such as locking and retrying).
 *
 * TODO: Consider if having pluggable decorators wouldn't be better to wrap them one by one also with internal ones.
 *
 * @author Marcin Zajączkowski, https://blog.solidsoft.info/
 */
class PowerStageDecorator {

    private static final int DEFAULT_STAGE_TIMEOUT_IN_SECONDS = 15 * 60

    private final Script pipelineScript
    private final PowerStageConfigView config   //Maybe only pass relevant part of configuration? - assuming decorators in use
    private final int numberOfAttempts

    PowerStageDecorator(Script pipelineScript, PowerStageConfigView config) {
        this.pipelineScript = pipelineScript
        this.config = config
        this.numberOfAttempts = config.maximumNumberOfAttempts
    }

    int decorateStageAndReturnedUpdateNextMilestoneNumber(String stageName, int initialNextMilestoneNumber, Closure stageBlock) {

        int nextMilestoneNumber = initialNextMilestoneNumber
        int currentAttempt = 0

        executeInScriptContext {
            retry(numberOfAttempts) {
                currentAttempt++    //TODO: Report feature request to have attempt number available in "it" within retry closure
                String executionName = prepareExecutionName(stageName, currentAttempt)
                echo "Executing ${executionName}..."

                lock(resource: generateLockResourceName(stageName), inversePrecedence: true) {
                    //TODO: Consider extra timeouts - including lock awaiting time or per whole stage (including all retries)
                    timeout([time: DEFAULT_STAGE_TIMEOUT_IN_SECONDS, unit: TimeUnit.SECONDS]) {
                        milestone nextMilestoneNumber++
                        stageBlock()
                        echo "Adjusting current milestone to maximum configured number of attempts"
                        while ((nextMilestoneNumber - initialNextMilestoneNumber) <= numberOfAttempts) {
                            milestone nextMilestoneNumber++
                        }
                    }
                }
            }
        }
        return nextMilestoneNumber
    }

    private def executeInScriptContext(Closure stageBlock) {
        //pipelineScript.with { stageBlock.call() } doesn't seem to work on Jenkins
        stageBlock.delegate = pipelineScript
        return stageBlock.call()
    }

    private String prepareExecutionName(String stageName, int currentAttempt) {
        return "stage '${stageName}' (attempt: ${currentAttempt}/${numberOfAttempts})"
    }

    private String generateLockResourceName(String stageName) {
        return "${pipelineScript.env.JOB_NAME}-${stageName}"
    }
}
