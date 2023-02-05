package com.talaria.portal.externalModel;

public record Message(String receiverMedium,
                      String senderMedium,
                      String message) {
}
