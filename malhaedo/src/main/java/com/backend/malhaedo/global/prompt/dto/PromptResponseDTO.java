package com.backend.malhaedo.global.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PromptResponseDTO {

    private Status status;
    private Result result;

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private String code;
        private String message;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String id;
        private String name;
        private String model;
        private String method;
        private String taskType;
        private int trainEpochs;
        private double learningRate;
        private String status;
        private StatusInfo statusInfo;
        private String createdClientType;
        private String createdDate;
        private String updatedDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusInfo {
        private String label;
        private Integer dataRows;
        private Integer numOfTokens;
        private Integer currStep;
        private Integer totalTrainSteps;
        private Integer currEpoch;
        private Integer totalTrainEpochs;
        private String estimatedTime;
        private Double trainLoss;
        private Boolean sendWeightSuccess;
        private String failureReason;
        private String message;
        private String endDatetime;
    }
}
