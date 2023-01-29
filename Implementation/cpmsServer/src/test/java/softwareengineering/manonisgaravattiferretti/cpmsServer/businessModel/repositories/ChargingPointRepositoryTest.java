package softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.dtos.IncludeBatteryDTO;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Battery;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.ChargingPoint;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.SpecialOffer;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.entities.Tariff;
import softwareengineering.manonisgaravattiferretti.cpmsServer.businessModel.repositories.aggregationResults.TariffUnwind;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChargingPointRepositoryTest {
    @Autowired
    private ChargingPointRepository chargingPointRepository;
    private final ChargingPoint chargingPoint = new ChargingPoint();
    private final Battery battery = new Battery();

    @BeforeEach
    void setUp() {
        chargingPoint.setId("abcd");
        battery.setBatteryId(1);
        battery.setMinLevel(10.0);
        battery.setMaxLevel(20.0);
        battery.setPercent(10.0);
        List<Battery> cpBatteries = List.of(battery);
        chargingPoint.setBatteries(cpBatteries);
    }

    @Test
    void updateBatteryTest() {
        chargingPointRepository.insert(chargingPoint);
        IncludeBatteryDTO includeBatteryDTO = new IncludeBatteryDTO();
        includeBatteryDTO.setMinLevel(5.0);
        includeBatteryDTO.setMaxLevel(70.0);
        includeBatteryDTO.setPercent(30.0);
        chargingPointRepository.updateBatteryEnergyFlow(includeBatteryDTO, "abcd", 1);
        Optional<ChargingPoint> chargingPointOptional = chargingPointRepository.findById("abcd");
        Assertions.assertTrue(chargingPointOptional.isPresent());
        Assertions.assertEquals(1, chargingPointOptional.get().getBatteries().size());
        Battery batteryModified = chargingPointOptional.get().getBatteries().get(0);
        Assertions.assertEquals(1, batteryModified.getBatteryId());
        Assertions.assertEquals(5.0, batteryModified.getMinLevel());
        Assertions.assertEquals(70.0, batteryModified.getMaxLevel());
        Assertions.assertEquals(30.0, batteryModified.getPercent());
        chargingPointRepository.deleteById("abcd");
    }

    @Test
    void tariffDeserializationTest() {
        SpecialOffer specialOffer = new SpecialOffer();
        specialOffer.setTariffId("12345");
        specialOffer.setStartTime(LocalTime.now());
        specialOffer.setEndTime(LocalTime.now());
        chargingPoint.setTariffs(List.of(specialOffer));
        chargingPointRepository.insert(chargingPoint);
        Optional<ChargingPoint> chargingPointOptional = chargingPointRepository.findById("abcd");
        Assertions.assertTrue(chargingPointOptional.isPresent());
        Assertions.assertEquals(1, chargingPointOptional.get().getTariffs().size());
        Tariff tariff = chargingPointOptional.get().getTariffs().get(0);
        Assertions.assertTrue(tariff instanceof SpecialOffer);
        Assertions.assertEquals("12345", tariff.getTariffId());
        chargingPointRepository.deleteById("abcd");
    }

    @Test
    void tariffUnwindAggregationTest() {
        ChargingPoint chargingPoint2 = new ChargingPoint();
        chargingPoint2.setId("ABCD");
        List<Tariff> tariffs = new ArrayList<>();
        List<Tariff> tariffs2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Tariff tariff = new Tariff();
            tariff.setTariffId(String.valueOf(i * 2));
            tariff.setLastUpdated(LocalDateTime.of(2010 + i, Month.MAY, 10, 5, 4));
            tariffs.add(tariff);
            Tariff tariff2 = new Tariff();
            tariff2.setTariffId(String.valueOf((i * 2) + 1));
            tariff2.setLastUpdated(LocalDateTime.of(2010 + i, Month.MAY, 11, 5, 4));
            tariffs2.add(tariff2);
        }
        chargingPoint.setTariffs(tariffs);
        chargingPoint2.setTariffs(tariffs2);
        chargingPointRepository.save(chargingPoint);
        chargingPointRepository.save(chargingPoint2);
        LocalDateTime start = LocalDateTime.of(2009, Month.MAY, 10, 5, 4);
        LocalDateTime end = LocalDateTime.of(2020, Month.MAY, 10, 5, 4);
        List<Tariff> tariffUnwinds = chargingPointRepository.findAllTariffsBetween(start, end, 0, 2)
                .getMappedResults().stream().map(TariffUnwind::getTariff).toList();
        System.out.println(tariffUnwinds);
        Assertions.assertEquals(2, tariffUnwinds.size());
        Assertions.assertEquals("3", tariffUnwinds.get(0).getTariffId());
        Assertions.assertEquals("2", tariffUnwinds.get(1).getTariffId());
        List<Tariff> tariffUnwinds1 = chargingPointRepository.findAllTariffsBetween(start, end, 2, 2)
                .getMappedResults().stream().map(TariffUnwind::getTariff).toList();
        System.out.println(tariffUnwinds);
        Assertions.assertEquals(2, tariffUnwinds1.size());
        Assertions.assertEquals("1", tariffUnwinds1.get(0).getTariffId());
        Assertions.assertEquals("0", tariffUnwinds1.get(1).getTariffId());
        chargingPointRepository.deleteById("abcd");
        chargingPointRepository.deleteById("ABCD");
    }
}