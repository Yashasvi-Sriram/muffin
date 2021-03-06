package org.muffin.muffin.daos;

import lombok.NonNull;

import org.muffin.muffin.beans.CinemaBuilding;
import org.muffin.muffin.beans.Genre;
import org.muffin.muffin.beans.Movie;

import java.util.List;
import java.util.Optional;

public interface CinemaBuildingDAO {
    public Optional<CinemaBuilding> get(final int cinemaBuildingId);

    public Optional<CinemaBuilding> create(final int ownerId, final String name, final String streetName, final String city, final String state, final String country, final String zip);

    public Optional<CinemaBuilding> get(final String name, final String streetName, final String city, final String state, final String country, final String zip);

    public List<CinemaBuilding> getByOwner(final int ownerId);

    public boolean delete(final int cinemaBuildingId, int ownerId);
}
