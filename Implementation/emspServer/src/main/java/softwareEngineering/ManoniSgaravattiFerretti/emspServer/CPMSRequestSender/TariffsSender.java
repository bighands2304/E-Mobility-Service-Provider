package softwareEngineering.ManoniSgaravattiFerretti.emspServer.CPMSRequestSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.ChargingPoint;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.SpecialOffer;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Model.Tariff;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingPointDataModel.Service.TariffService;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.OcpiDTOs.TariffDTO;

import java.util.List;
@Service
public class TariffsSender {
    @Autowired
    TariffService tariffService;

    private String ocpiPath="/ocpi/cpo";
    private RestTemplate restTemplate = new RestTemplate();

    public void getTariffs(ChargingPoint cp) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", cp.getCpo().getTokenEmsp());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(cp.getCpo().getCpmsUrl() + ocpiPath + "/tariffs").encode().toUriString();

        ParameterizedTypeReference<Page<TariffDTO>> typo = new ParameterizedTypeReference<Page<TariffDTO>>() {
        };
        ResponseEntity<Page<TariffDTO>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                entity,
                typo
        );

        List<TariffDTO> tariffs = response.getBody().getContent();
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
            if (t.isSpecialOffer()){
                SpecialOffer newSpecialOffer = (SpecialOffer) newTariff;
                newSpecialOffer.setStartTime(t.getStartTime());
                newSpecialOffer.setEndTime(t.getEndTime());
                newSpecialOffer.setMinKWh(t.getMinKWh());
                newSpecialOffer.setMaxKWh(t.getMaxKWh());
                newSpecialOffer.setMinCurrent(t.getMinCurrent());
                newSpecialOffer.setMaxCurrent(t.getMaxCurrent());
                newSpecialOffer.setMinDuration(t.getMinDuration());
                newSpecialOffer.setMaxDuration(t.getMaxDuration());
                newSpecialOffer.setDaysOfTheWeek(t.getDaysOfTheWeek());
                tariffService.save(newSpecialOffer);
            }else {
                tariffService.save(newTariff);
            }
        }
    }
}
