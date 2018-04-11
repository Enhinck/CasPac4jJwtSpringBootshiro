package com.enhinck.demo.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Entity
@Data
@Access(AccessType.FIELD)
public class Demo {
	@Id
	@GeneratedValue
	private Integer id;
	private Integer status;
	private String name;
	private Date createDate;
	private Date updateDate;
	
	public static void main(String[] args) {
		Demo d = new Demo();
		d.setId(1);
		d.setStatus(2);
		d.setName("1111");
		System.out.println(d.toString());
	}
}
