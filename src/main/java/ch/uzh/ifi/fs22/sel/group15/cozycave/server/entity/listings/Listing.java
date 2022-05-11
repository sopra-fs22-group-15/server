package ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.AssertionsUtils;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "listings")
@AllArgsConstructor @Getter @Setter @ToString @RequiredArgsConstructor
public class Listing implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)
    private Date creationDate;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location address;

    @Column(name = "published", nullable = false)
    private Boolean published;

    @Column(name = "sqm")
    private Double sqm;

    @Column(name = "listing_type")
    private ListingType listingType;

    @Column(name = "furnished")
    private Boolean furnished;

    @ElementCollection(targetClass = Gender.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "listings_gender")
    @Column(name = "available_to")
    private List<Gender> availableTo;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "rent")
    private Double rent;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "rooms")
    private Double rooms;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    public boolean isReadyToPublish() {
        return StringUtils.hasText(this.getTitle())
            && StringUtils.hasText(this.getDescription())
            && this.getAddress() != null
            && this.sqm != null
            && this.sqm >= 0
            && this.getListingType() != null
            && !AssertionsUtils.isEmpty(this.getAvailableTo())
            && this.available != null
            && this.rent != null
            && this.rent >= 0
            && this.deposit != null
            && this.deposit >= 0
            && this.rooms != null
            && this.rooms >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Listing listing = (Listing) o;
        return id != null && Objects.equals(id, listing.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Listing clone() {
        return new Listing(
            this.getId(),
            this.getCreationDate(),
            this.getTitle(),
            this.getDescription(),
            this.getAddress(),
            this.getPublished(),
            this.getSqm(),
            this.getListingType(),
            this.getFurnished(),
            List.copyOf(this.getAvailableTo()),
            this.getAvailable(),
            this.getRent(),
            this.getDeposit(),
            this.getRooms(),
            this.getPublisher()
        );
    }
}
