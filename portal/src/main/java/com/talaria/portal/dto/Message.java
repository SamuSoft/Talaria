package com.talaria.portal.dto;

public record Message(String receiverMedium,
                      String senderMedium,
                      String message) {
}
