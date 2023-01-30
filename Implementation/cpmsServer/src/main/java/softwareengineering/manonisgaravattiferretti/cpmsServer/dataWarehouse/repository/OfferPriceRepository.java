package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.OfferPrice;

@Repository
public interface OfferPriceRepository extends CassandraRepository<OfferPrice, DimensionsPrimaryKey> {
}
