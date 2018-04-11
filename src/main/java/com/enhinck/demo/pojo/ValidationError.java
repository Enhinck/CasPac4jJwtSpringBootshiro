package com.enhinck.demo.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ValidationError {
    String message;
    String name;
    Object value;
}
