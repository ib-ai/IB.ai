package com.ibdiscord.ibai.repositories;

import com.ibdiscord.ibai.entities.Tags;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tags, Tags.CompositePK> {
}
