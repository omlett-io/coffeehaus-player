package io.omlett.coffeehaus.player.service.persistence.converter;

import com.google.protobuf.MessageLite;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@AllArgsConstructor
public class ProtobufWriteConverter<T extends MessageLite> implements Converter<T, byte[]> {

  @Override
  public byte[] convert(T t) {
    return t.toByteArray();
  }
}
