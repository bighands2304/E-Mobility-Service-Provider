package softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.DimensionsPrimaryKey;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.entity.OfferPrice;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.OfferPriceByYear;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.groupByDtos.OfferPriceByYearMonth;
import softwareengineering.manonisgaravattiferretti.cpmsServer.dataWarehouse.repository.OfferPriceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfferPriceService {
    private final OfferPriceRepository offerPriceRepository;

    @Autowired
    public OfferPriceService(OfferPriceRepository offerPriceRepository) {
        this.offerPriceRepository = offerPriceRepository;
    }

    public void insert(OfferPrice offerPrice) {
        offerPriceRepository.insert(offerPrice);
    }

    public void insertBatch(List<OfferPrice> offerPrice) {
        offerPriceRepository.insert(offerPrice);
    }

    public List<OfferPrice> findBetween(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return offerPriceRepository.findBetween(cpId, dsoId, dateFrom, dateTo);
    }

    public List<OfferPriceByYear> findBetweenByYear(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return offerPriceRepository.findBetweenGroupByYear(cpId, dsoId, dateFrom, dateTo);
    }

    public List<OfferPriceByYearMonth> findBetweenByYearMonth(String cpId, String dsoId, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return offerPriceRepository.findBetweenGroupByYearAndMonth(cpId, dsoId, dateFrom, dateTo);
    }

    public Optional<OfferPrice> find(String cpId, String dsoId, LocalDateTime timestamp) {
        return offerPriceRepository.findById(new DimensionsPrimaryKey(dsoId, cpId, timestamp));
    }
}
