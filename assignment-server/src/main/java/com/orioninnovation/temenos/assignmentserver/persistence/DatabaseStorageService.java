package com.orioninnovation.temenos.assignmentserver.persistence;


import com.orioninnovation.temenos.assignmentserver.model.Calculation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseStorageService implements StorageService {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseStorageService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Calculation calculation) {
        jdbcTemplate.update(
                "INSERT INTO calculation (id, number, threads, status) VALUES (?, ?, ?, ?)",
                calculation.getId(),
                calculation.getNumber(),
                calculation.getThreadCount(),
                calculation.getStatus().name()
        );
    }

    @Override
    public void update(Calculation calculation) {
        jdbcTemplate.update(
                "UPDATE calculation SET result=?, status=? WHERE id=?",
                calculation.getResult(),
                calculation.getStatus().name(),
                calculation.getId()
        );
    }

    @Override
    public Calculation get(String id) {

        return jdbcTemplate.queryForObject(
                "SELECT * FROM calculation WHERE id=?",
                new Object[]{id},
                (rs, rowNum) -> new Calculation(
                        rs.getLong("number"),
                        rs.getInt("threads")
                )
        );
    }
}
