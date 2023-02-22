package com.github.tomivv.homeinventoryserver.item;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ItemRepository extends CrudRepository<Item, String> {
}
