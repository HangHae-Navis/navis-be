package com.hanghae.navis.messenger.dto;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.Objects;

@Data
@Builder
public class ChatingRoom {
    private String roomId;
    private LinkedList<String> users;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChatingRoom other = (ChatingRoom) obj;
        return Objects.equals(roomId, other.roomId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }
}