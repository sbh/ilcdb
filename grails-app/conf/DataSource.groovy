dataSource {
    pooled = false
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='org.hibernate.cache.EhCacheProvider'
}

//environment specific settings
environments {
    // development {
    //     dataSource {
    //         dbCreate = "update"
    //         driverClassName = "com.mysql.cj.jdbc.Driver"
    //         url = "jdbc:mysql://localhost/ilcdb"
    //         username = System.getenv("MYSQL_USER")
    //         password =  System.getenv("MYSQL_PASSWORD")
    //         //logSql = true
    //     }
    // }
    // test {
    //     dataSource {
    //         dbCreate = "update"
    //         url = "jdbc:derby:db"
    //         driverClassName = "org.apache.derby.jdbc.EmbeddedDriver"
    //     }
    // }
    production {
        dataSource {
            dbCreate = "update"
            driverClassName = "com.mysql.cj.jdbc.Driver"
            url = "jdbc:mysql://${System.getenv('MYSQL_HOST')}:3306/${System.getenv('MYSQL_DATABASE')}"
            username = System.getenv("MYSQL_USER")
            password =  System.getenv("MYSQL_PASSWORD")
        }
    }
}
