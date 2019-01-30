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
package info.solidsoft.jenkins.powerstage

import info.solidsoft.jenkins.powerstage.stage.PowerStageConfig
import info.solidsoft.jenkins.powerstage.stage.PowerStageConfigView
import info.solidsoft.jenkins.powerstage.stage.PowerStageCreator;

/**
 * An entry point to stage orchestration in a pipeline.
 *
 * It requests stage creation (with appropriate type/role and configuration) and keep track it order in a pipeline.
 *
 * @author Marcin Zajączkowski, https://blog.solidsoft.info/
 */
@SuppressWarnings("unused")
class PowerStageOrchestrator {  //TODO: PowerPipelineOrchestrator?

    private static final int DEFAULT_INITIAL_NEXT_MILESTONE_NUMBER = 1

    private final Script pipelineScript
    private final PowerStageConfigView globalConfig

    int nextMilestoneNumber

    PowerStageOrchestrator(Script pipelineScript, int initialNextMilestoneNumber, PowerStageConfigView globalConfig) {
        this.pipelineScript = pipelineScript
        this.nextMilestoneNumber = initialNextMilestoneNumber
        this.globalConfig = globalConfig
    }

//    //TODO: That construction fails on Jenkins with:
//    //    ProxyException: org.codehaus.groovy.runtime.typehandling.GroovyCastException: Cannot cast object 'info.solidsoft.jenkins.powerstage.stage.PowerStageConfig@1ab38aa'
//    //    with class 'info.solidsoft.jenkins.powerstage.stage.PowerStageConfig' to class 'info.solidsoft.jenkins.powerstage.PowerStageOrchestrator'
//    //Workaround applied...
//    static PowerStageOrchestrator stageOrchestrator(Script pipelineScript, int initialNextMilestoneNumber = DEFAULT_INITIAL_NEXT_MILESTONE_NUMBER, PowerStageConfigView globalConfig = createConfig()) {

    static PowerStageOrchestrator stageOrchestrator(Script pipelineScript, int initialNextMilestoneNumber = DEFAULT_INITIAL_NEXT_MILESTONE_NUMBER) {
        return stageOrchestrator(pipelineScript, initialNextMilestoneNumber, createConfig())
    }

    static PowerStageOrchestrator stageOrchestrator(Script pipelineScript, int initialNextMilestoneNumber = DEFAULT_INITIAL_NEXT_MILESTONE_NUMBER, PowerStageConfigView globalConfig) {
        return new PowerStageOrchestrator(pipelineScript, initialNextMilestoneNumber, globalConfig)
    }

    void simpleStage(String stageName, Closure stageBlock) {
//        customStage(stageName, stageBlock)  //TODO: Why it fails silently???
        customStage(stageName, createConfig {
            maximumNumberOfAttempts = 1
        }, stageBlock)
    }

    void defaultStage(String stageName, Closure stageBlock) {
        customStage(stageName, createConfig(), stageBlock)
    }

    void customStage(String stageName, PowerStageConfigView config = globalConfig, Closure stageBlock) {
        nextMilestoneNumber = new PowerStageCreator(pipelineScript, config)
            .createStageAndReturnedUpdateNextMilestoneNumber(stageName, nextMilestoneNumber, stageBlock)
    }

    static PowerStageConfigView createConfig(@DelegatesTo(PowerStageConfig) Closure configBlock = {}) {
        PowerStageConfig config = new PowerStageConfig()
        configBlock.delegate = config
        configBlock.resolveStrategy = Closure.DELEGATE_FIRST    //without that changes from configBlock are not applied in config
        configBlock()
        return config
    }
}
