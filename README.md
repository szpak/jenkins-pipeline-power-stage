# jenkins-pipeline-power-stage

A convenient Jenkins Pipeline stage abstraction providing power features which are not available out of the box in the Jenkins Pipeline stage
(and need to be in-house programmed or applied explicitly), such as (manual) stage retrying, per stage synchronization or stage superseding.

## Usage

The projects is currently not available from the Maven Central repository. It has to be added manually to your shared library definition,
for example as a Git sub-module (preferably by using a release tag).

## Additional information

jenkins-pipelineScript-power-stage was created by Marcin Zajączkowski with the help of the
[contributors](https://github.com/szpak/jenkins-pipeline-power-stage/graphs/contributors).
The author can be contacted directly via email: `mszpak ATT wp DOTT pl`.
There is also Marcin's blog available: [Solid Soft](https://blog.solidsoft.info) - working code is not enough.

The first version of this project was based on the author's experience gained while working on delivery pipelines for [Pragmatic Coders](https://pragmaticcoders.com/),
a software house providing blockchain and FinTech development services.

Inspired by the Patrick Wolf's [article](https://jenkins.io/blog/2016/10/16/stage-lock-milestone/).  

The project [changelog](https://github.com/szpak/jenkins-pipeline-power-stage/releases).

**WARNING**. The project is a very early stage of development and may change in a backward incompatible way. 

The plugin is licensed under the terms of [the Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).
