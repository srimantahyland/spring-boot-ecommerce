package com.luv2code.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
	
	private EntityManager entityManager;
	
	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}
	
	
		
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		
		HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
		
		//disable http method for Product: put, post, delete
		config.getExposureConfiguration()
		.forDomainType(Product.class)
		.withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
		.withCollectionExposure((metdata, httpMethods ) -> httpMethods.disable(unsupportedActions));
		
		//disable http method for ProductCategory: put, post, delete
		config.getExposureConfiguration()
		  .forDomainType(ProductCategory.class)
		  .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
		  .withCollectionExposure((metdata, httpMethods ) -> httpMethods.disable(unsupportedActions));
		
		exposeIds(config);
	}



	private void exposeIds(RepositoryRestConfiguration config) {
		// expose entity ids
		// get a list of all entities from entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		// create a list of entity types
		List<Class> entityClasses = new ArrayList();
		
		// get the entity type for the entities
		for (EntityType tempEntityType: entities) {
			entityClasses.add(tempEntityType.getJavaType());
		}
		
		// expose the entity ids for the array of  entity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
		
		
	}

}
