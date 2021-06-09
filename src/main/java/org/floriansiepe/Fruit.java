package org.floriansiepe;

import java.util.Objects;

public class Fruit {
    public Long id;
    public String name;
    public String description;

    public Fruit() {
    }

    public Fruit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fruit fruit = (Fruit) o;

        return Objects.equals(id, fruit.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
