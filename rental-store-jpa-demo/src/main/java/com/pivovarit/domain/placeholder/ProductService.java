package com.pivovarit.domain.placeholder;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

public class ProductService {

    private final JdbcClient jdbcClient;

    public ProductService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Product> findAll() {
        return jdbcClient.sql("SELECT * FROM products").query(asProduct()).list();
    }

    public List<Product> findAllActive() {
        return jdbcClient.sql("SELECT * FROM products WHERE active = ?")
          .param(true)
          .query(asProduct()).list();
    }

    public long save(String name, long priceCents, boolean active) {
        return jdbcClient.sql("""
            INSERT INTO products (name, price_cents, active)
            VALUES (?, ?, ?)
            RETURNING id
            """)
          .param(name)
          .param(priceCents)
          .param(active)
          .query(Long.class)
          .single();
    }

    private static RowMapper<Product> asProduct() {
        return (rs, _) -> new Product(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getLong("price_cents"),
          rs.getBoolean("active"),
          rs.getTimestamp("created_at").toInstant()
        );
    }
}
