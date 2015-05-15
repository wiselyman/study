## 0.1 Spring Data Repository
- 本节介绍是关于**Spring Data**的通用只是,当然是完全适合于**Spring Data JPA**;
- **Spring Data JPA**是**Spring Data**家族的重要成员;
- **Spring Data repository abstraction**的目标是极大的减少数据访问层(各种持久化方案)的模板代码(经常要重复写的冗余代码);
- **Spring Data repository abstraction**核心接口是**Repository**,它管理一个实体类及实体类的id类型;
- **CrudRepository**为其管理的实体类提供CRUD(增删查改)功能
    ```
    public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
        <S extends T> S save(S entity);
        T findOne(ID primaryKey);
        Iterable<T> findAll();
        Long count(); <4>
        void delete(T entity);
        boolean exists(ID primaryKey);
       ...
    }
    ```

- **Spring Data JPA**有**JpaRepository**继承**CrudRepository**,只针对于JPA方式的数据持久化操作;
- **PagingAndSortingRepository**接口继承自**CrudRepository**,顾名思义具有分页和排序的额外功能;
    ```
    public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
        Iterable<T> findAll(Sort sort);
        Page<T> findAll(Pageable pageable);
    }
    ```

- 定义自己的实体类repository
    ```
    public interface UserRepository extends CrudRepository<User, Long> {

    }
    ```

## 0.2 查询方法

- 通过Spring Data

- Spring is instructed to scan com.acme.repositories and all its sub-packages
for interfaces extending Repository or one of its sub-interfaces. For each interface found, the
infrastructure registers the persistence technology-specific FactoryBean to create the appropriate
proxies that handle invocations of the query methods. Each bean is registered under a bean name that
is derived from the interface name, so an interface of UserRepository would be registered under
userRepository. The base-package attribute allows wildcards, so that you can define a pattern of
scanned packages.