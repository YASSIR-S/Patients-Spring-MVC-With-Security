package ma.emsi.patientsmvc.web;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import ma.emsi.patientsmvc.entities.Patient;
import ma.emsi.patientsmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping("/index")
    public String patient(Model model, @RequestParam(name="page",defaultValue = "0") int page,
                          @RequestParam(name = "size",defaultValue = "5") int size,
                          @RequestParam(name = "keyword",defaultValue = "")String keyword){
        Page<Patient> pagepatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));

        model.addAttribute("listpatient",pagepatients.getContent()) ;
        model.addAttribute("pages",new int[pagepatients.getTotalPages()] ) ;

        model.addAttribute("currentpage",page) ;
        model.addAttribute("keyword",keyword);

        return "patients" ;
    }


    @GetMapping("/delete")
    public String delete(Long id,String keyword ,int page){
        patientRepository.deleteById(id);
        return "redirect:/index?page="+page+"&keyword="+keyword  ;
    }


    @GetMapping("/")
    public String home(){

        return "home" ;
    }

    @GetMapping("/formPatients")
    public String formPatient(Model model){

        model.addAttribute("patient",new Patient());
        return "formPatients" ;
    }


    @GetMapping("/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult){

        if (bindingResult.hasErrors()) return "formPatients" ;
        patientRepository.save(patient);
        return "redirect:/index";
    }

    @GetMapping("/editPatient")
    public String editPatient(Model model , Long id ,String keyword ,int page){

        Patient patient= patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient Introuvable") ;

        model.addAttribute("patient",patient) ;
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page) ;
        return "editPatient" ;

    }


}
