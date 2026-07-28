# Product Cataloge

## Plan 

So this should be a service that will get all the products in our catalog for the front end. 
This means we will need to define what a product is. Create the JPA, entity, Controller and service for it. 
We will also be trying to use Redis read-through caching what ever that is for faster retrieval. 

So the actual plan.

- Database layer
  - JPA, Entities, Product & Category (maybe), mapped to a schema
  
- Service layer
  - Business rules around inventory management, product updates, price validation, transactional boundaries
  
- Controller layer
  - Rest endpoint with DTOs, 
  - GET /v1/products - get all 
  - GET /v1/products/{id} - get by id 
  - POST /v1/products - create
  - PUT /v1/products - update
  - PATCH /v1/products - adjust stock quant


- Product
  - id
  - sku
  - name
  - description
  - quantity
  - price
  - created_at
  - updated_at - for concurrency
  

- Category
  - id
  - name
  - slug? 
  - created_at

So bizarrely gordon already put most of that together in the initial schema so im gonna go with it. 

So entity, JPA, blank Service interface

Then controller & DTO

then service impl and we can start learning about redis.

finish controller 

then done? 

For this version. We start with just the API routes we can come back for any specific elements later as a feature branch.

Testing: 

Once its all together integration tests will be straight forward. 

Service tests will depend on how mockable it all is 

JPA shouldnt need any tests so p1 just go 

p2 mostly stubs anyway 

p3 starts with TDD/BDD 




So yeah I think thats the plan. We will have a stab and see what happens. 