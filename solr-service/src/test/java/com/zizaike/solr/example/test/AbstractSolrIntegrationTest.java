/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zizaike.solr.example.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zizaike.solr.example.model.Product;

@ContextConfiguration(locations = {"classpath:/spring/applicationContext.xml"})
public abstract class AbstractSolrIntegrationTest extends AbstractTestNGSpringContextTests {

  protected List<Product> createProductList(int nrProducts) {
    List<Product> products = new ArrayList<Product>(nrProducts);
    for (int i = 0; i < nrProducts; i++) {
      products.add(createProduct(i));
    }
    return products;
  }

  protected Product createProduct(int id) {
    Product product = new Product();
    product.setId(Integer.toString(id));
    product.setAvailable(id % 2 == 0);
    product.setName("product-" + id);
    product.setPopularity(id * 10);
    product.setPrice((float) id * 100);
    product.setWeight((float) id * 2);
    return product;
  }
}
