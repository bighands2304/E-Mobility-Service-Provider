package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPointOperator;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.TariffDTO;

import java.util.List;
import java.util.Objects;

@Service
public class TariffsSender {
    @Autowired
    TariffService tariffService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void getTariffs(ChargingPointOperator cpo) {
        try {
            Thread.sleep(1000*60);
        }catch (Exception e){}

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cpo.getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String ocpiPath = "/ocpi/cpo";
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cpo.getCpmsUrl() + ocpiPath + "/tariffs").encode().toUriString();

        ParameterizedTypeReference<Page<TariffDTO>> typo = new ParameterizedTypeReference<>() {};
        ResponseEntity<Page<TariffDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        List<TariffDTO> tariffs = Objects.requireNonNull(response.getBody()).getContent();
        for (TariffDTO t: tariffs) {
            Tariff newTariff = tariffService.getTariffById(t.getTariffId());
            if (newTariff==null){
                newTariff=new Tariff();
            }
            newTariff.setTariffId(t.getTariffId());
            newTariff.setSocketType(t.getSocketType());
            newTariff.setPrice(t.getPrice());
            newTariff.setStepSize(t.getStepSize());
            newTariff.setStartDate(t.getStartDate());
            newTariff.setEndDate(t.getEndDate());
            newTariff.setIsSpecialOffer(t.isSpecialOffer());

            newTariff.setStartTime(t.getStartTime());
            newTariff.setEndTime(t.getEndTime());
            newTariff.setMinKWh(t.getMinKWh());
            newTariff.setMaxKWh(t.getMaxKWh());
            newTariff.setMinCurrent(t.getMinCurrent());
            newTariff.setMaxCurrent(t.getMaxCurrent());
            newTariff.setMinDuration(t.getMinDuration());
            newTariff.setMaxDuration(t.getMaxDuration());
            newTariff.setDaysOfTheWeek(t.getDaysOfTheWeek());

            tariffService.save(newTariff);
        }
    }
}
