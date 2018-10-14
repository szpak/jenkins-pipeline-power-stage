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

//TODO: Think about using builder pattern
//TODO: Is it needed to add also PowerStageGlobalConfing (per pipeline)?
class PowerStageConfig implements PowerStageConfigView {

    private static final int DEFAULT_MAXIMUM_NUMBER_OF_ATTEMPTS = 3

    int maximumNumberOfAttempts = DEFAULT_MAXIMUM_NUMBER_OF_ATTEMPTS
}
