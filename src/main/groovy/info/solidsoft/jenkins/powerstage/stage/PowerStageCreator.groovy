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

/**
 * A stage creator.
 *
 * In addition to a stage creation it provides also other stage functionality (such as a separate stage for a manual approval request).
 *
 * @author Marcin Zajączkowski, https://blog.solidsoft.info/
 */
class PowerStageCreator {

    private final Script pipelineScript  //in fact org.jenkinsci.plugins.workflow.cps.CpsScript

    PowerStageCreator(Script pipelineScript) {
        this.pipelineScript = pipelineScript
    }

    int createStageAndReturnedUpdateNextMilestoneNumber(String stageName, int initialNextMilestoneNumber, Closure stageBlock) {

        int nextMilestoneNumber = initialNextMilestoneNumber
        executeInScriptContext {

            stage(stageName) {
                echo "Executing stage {$stageName}"
                milestone nextMilestoneNumber++
                lock(resource: "${env.JOB_NAME}-stageName", inversePrecedence: true) {
                    milestone nextMilestoneNumber++
                    stageBlock()
                }
                milestone nextMilestoneNumber++
            }
        }

        return nextMilestoneNumber
    }

    private def executeInScriptContext(Closure stageBlock) {
        //pipelineScript.with { stageBlock.call() } doesn't seem to work on Jenkins
        stageBlock.delegate = pipelineScript
        return stageBlock.call()
    }
}
