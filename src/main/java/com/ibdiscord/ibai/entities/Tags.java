package com.ibdiscord.ibai.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@IdClass(Tags.CompositePK.class)
@Entity
@Table(name = "tags")
public final class Tags {

    @NoArgsConstructor
    @AllArgsConstructor
    public static final class CompositePK implements Serializable {
        private long guild_id;
        private String name;
    }

    @Id
    @Column(name = "guild_id")
    private long guild_id;

    @Id
    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "output", length = 512)
    private String output;

    @Column(name = "disabled")
    private boolean disabled;

    public Tags(long guild_id, String name, String output) {
        this.guild_id = guild_id;
        this.name = name;
        this.output = output;
        this.disabled = false;
    }

    public Tags(long guild_id, String name, String output, boolean disabled) {
        this.guild_id = guild_id;
        this.name = name;
        this.output = output;
        this.disabled = disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tags tags = (Tags) o;

        return guild_id == tags.guild_id && name.equals(tags.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guild_id, name);
    }
}
