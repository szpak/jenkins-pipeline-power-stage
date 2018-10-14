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

    private final Script pipelineScript

    int nextMilestoneNumber

    PowerStageOrchestrator(Script pipelineScript, int initialNextMilestoneNumber = 1) {
        this.pipelineScript = pipelineScript
        this.nextMilestoneNumber = initialNextMilestoneNumber
    }

    void simpleStage(String stageName, Closure stageBlock) {
        nextMilestoneNumber = new PowerStageCreator(pipelineScript, createConfig())
            .createStageAndReturnedUpdateNextMilestoneNumber(stageName, nextMilestoneNumber, stageBlock)
    }

    void customStage(String stageName, PowerStageConfigView config = createConfig(), Closure stageBlock) {
        nextMilestoneNumber = new PowerStageCreator(pipelineScript, config)
            .createStageAndReturnedUpdateNextMilestoneNumber(stageName, nextMilestoneNumber, stageBlock)
    }

    static PowerStageConfigView createConfig(@DelegatesTo(PowerStageConfig) Closure configBlock = {}) {
        PowerStageConfig config = new PowerStageConfig()
        configBlock.delegate = config
        configBlock()
        return config
    }
}
