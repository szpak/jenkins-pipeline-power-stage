package info.solidsoft.jenkins.powerstage.stage

//TODO: Think about using builder pattern
class PowerStageConfig implements PowerStageConfigView {

    private static final int DEFAULT_MAXIMUM_NUMBER_OF_ATTEMPTS = 3

    int maximumNumberOfAttempts = DEFAULT_MAXIMUM_NUMBER_OF_ATTEMPTS
}
