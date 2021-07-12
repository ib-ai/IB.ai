package com.ibdiscord.ibai.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public final class FilterEntry {

    @Column(name = "value", length = 512)
    private String value;

    @Column(name = "notify")
    private boolean notify;

    public FilterEntry(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterEntry that = (FilterEntry) o;
        return notify == that.notify && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, notify);
    }
}
