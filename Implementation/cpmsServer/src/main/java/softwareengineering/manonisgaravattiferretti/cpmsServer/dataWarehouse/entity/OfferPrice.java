package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "offer_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferPrice {
    @PrimaryKey
    private DimensionsPrimaryKey dimensionsPrimaryKey;
    @Column(value = "price")
    private double price;
}
