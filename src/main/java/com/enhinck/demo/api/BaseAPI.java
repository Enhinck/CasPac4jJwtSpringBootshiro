package com.enhinck.demo.api;

import com.enhinck.demo.entity.Demo;

import lombok.Getter;


@Getter
public abstract class BaseAPI {
	protected ThreadLocal<Demo> demo = new ThreadLocal<>();
}
