package io.omlett.coffeehaus.player.service.persistence.converter;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
@AllArgsConstructor
public class ProtobufReadConverter<T extends MessageLite> implements Converter<byte[], T> {

  private Parser<T> parser;

  @Override
  public T convert(byte[] bytes) {
    try {
      return parser.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      log.error("Protobuf failed to parse message. Here's how far it got:");
      log.error(e.getUnfinishedMessage().toString());
      throw new IllegalStateException(e.getMessage());
    }
  }
}
