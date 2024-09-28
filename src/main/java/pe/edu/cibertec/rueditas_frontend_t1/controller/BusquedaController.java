package pe.edu.cibertec.rueditas_frontend_t1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.rueditas_frontend_t1.dto.BusquedaRequestDTO;
import pe.edu.cibertec.rueditas_frontend_t1.dto.BusquedaResponseDTO;
import pe.edu.cibertec.rueditas_frontend_t1.viewmodel.BusquedaModel;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        model.addAttribute("busquedaModel", new BusquedaModel("00", "", "", "", "", "", ""));
        return "inicio";
    }

    @PostMapping("/buscar")
    public String busqueda(@RequestParam("placa") String placa, Model model) {
        // Validando

        if (placa == null || placa.trim().isEmpty()) {
            model.addAttribute("busquedaModel", new BusquedaModel("01", "Error en la busqueda de placa", "","", "","", ""));
            return "inicio";
        }

        try {
            // Invocando el servicio de busqueda
            String endpoint = "http://localhost:8080/busqueda/buscar";
            BusquedaRequestDTO buscarRequestDTO = new BusquedaRequestDTO(placa);
            BusquedaResponseDTO busquedaResponseDto = restTemplate.postForObject(endpoint, buscarRequestDTO, BusquedaResponseDTO.class);

            if (busquedaResponseDto != null && "00".equals(busquedaResponseDto.codigo())) {
                model.addAttribute("busquedaDTO", busquedaResponseDto);
                return "principal";
            } else {
                model.addAttribute("busquedaModel", new BusquedaModel("02", "No se encontraron registros de la placa ingresada", "", "", "","", ""));
                return "inicio";
            }

        } catch (Exception e) {
            model.addAttribute("busquedaModel", new BusquedaModel("99", "Error: Problemas en la busqueda", "", "", "", "", ""));
            System.out.println(e.getMessage());
            return "inicio";
        }
    }
}
