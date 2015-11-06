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
package com.zizaike.solr.example.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;

import com.zizaike.solr.example.model.Product;
import com.zizaike.solr.example.model.SearchableProduct;
import com.zizaike.solr.example.repository.CustomSolrRepository;

/**
 * 
 * ClassName: CustomSolrRepositoryImpl <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2015年10月26日 下午6:06:01 <br/>  
 *  
 * @author snow.zhang  
 * @version   
 * @since JDK 1.7
 */
public class CustomSolrRepositoryImpl implements CustomSolrRepository {

	private SolrOperations solrOperation;

	public CustomSolrRepositoryImpl() {
		super();
	}

	public CustomSolrRepositoryImpl(SolrOperations solrOperation) {
		super();
		this.solrOperation = solrOperation;
	}

	@Override
	public Page<Product> findProductsByCustomImplementation(String value, Pageable page) {
		return solrOperation.queryForPage(new SimpleQuery(new SimpleStringCriteria("name:" + value)).setPageRequest(page),
				Product.class);
	}

	@Override
	public void updateProductCategory(String productId, List<String> categories) {
		PartialUpdate update = new PartialUpdate(SearchableProduct.ID_FIELD, productId);
		update.setValueOfField(SearchableProduct.CATEGORY_FIELD, categories);

		solrOperation.saveBean(update);
		solrOperation.commit();
	}

}
