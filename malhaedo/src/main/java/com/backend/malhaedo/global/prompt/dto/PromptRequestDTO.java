package com.backend.malhaedo.global.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PromptRequestDTO {

    @Getter
    @AllArgsConstructor
    public static class ReplyPromptRequestDTO {
        private String name;
        private String model;
        private String tuningType;
        private String taskType;
        private int trainEpochs;
        private String learningRate;
        private String trainingDatasetFilePath;
        private String trainingDatasetBucket;
        private String trainingDatasetAccessKey;
        private String trainingDatasetSecretKey;
    }
}
