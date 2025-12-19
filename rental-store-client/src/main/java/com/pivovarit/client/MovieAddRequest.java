package com.pivovarit.client;

public record MovieAddRequest(
  long id,
  String title,
  String type) {
}
