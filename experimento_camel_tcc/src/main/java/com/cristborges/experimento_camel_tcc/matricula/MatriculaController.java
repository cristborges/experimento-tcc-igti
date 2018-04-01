package com.cristborges.experimento_camel_tcc.matricula;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AutoPopulatingList;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cristborges.experimento_camel_tcc.aluno.Aluno;
import com.cristborges.experimento_camel_tcc.aluno.AlunoService;
import com.cristborges.experimento_camel_tcc.curso.Curso;
import com.cristborges.experimento_camel_tcc.curso.CursoService;

@Controller
public class MatriculaController {

	private static final Logger logger = LoggerFactory.getLogger(MatriculaController.class);

	@Inject
	private MatriculaService matriculaService;

	@Inject
	private CursoService cursoService;

	@Inject
	private AlunoService alunoService;

	private Map<String, Curso> cursosCache = new HashMap<>();

	@InitBinder
	protected void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(Curso.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				this.setValue(cursosCache.get(text));
			}

			@Override
			public String getAsText() {
				return this.getValue() == null ? null : ((Curso) this.getValue()).getCodigo();
			}
		});
	}

	@RequestMapping(value = "/home")
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/cadastroMatriculas", method = RequestMethod.GET)
	public String cadastroMatriculas(@ModelAttribute("matriculasAlunoForm") MatriculasAluno matriculasAluno, Model model) {
		populateDefaultModel(model, matriculasAluno);

		return "cadastroMatriculas";
	}

	@RequestMapping(value = "/salvarMatriculasAluno", method = RequestMethod.POST)
	public String salvarMatriculasAluno(@ModelAttribute("matriculasAlunoForm") @Valid MatriculasAluno matriculasAluno, BindingResult bindingResult, Model model, final RedirectAttributes redirectAttributes) {
		logger.debug("salvarMatriculasAluno() : {}", matriculasAluno);

		if (bindingResult.hasErrors()) {
			populateDefaultModel(model);

			return "cadastroMatriculas";
		} else {
			matriculaService.salvarMatriculasAluno(matriculasAluno);

			redirectAttributes.addFlashAttribute("msg", "As matrículas informadas foram cadastradas com sucesso.");

			return "redirect:/pesquisaMatriculas";
		}
	}

	@RequestMapping(value = "/pesquisaMatriculas", method = RequestMethod.GET)
	public String pesquisaMatriculas(Model model) {
		model.addAttribute("matriculas", matriculaService.getMatriculas());

		return "pesquisaMatriculas";
	}

	@ResponseBody
	@RequestMapping(value = "/consultarAlunoByCpf", method = RequestMethod.POST)
	public Aluno consultarAluno(@RequestBody String cpf) {
		return alunoService.getAlunoByCpf(cpf);
	}

	@ResponseBody
	@RequestMapping(value = "/consultarMatricula/{id}", method = RequestMethod.GET)
	public Matricula consultarMatricula(@PathVariable("id") Long id) {
		return matriculaService.getMatricula(id);
	}

	private void populateDefaultModel(Model model, MatriculasAluno matriculasAluno) {
		matriculasAluno.setAluno(new Aluno());
		matriculasAluno.setMatriculas(new AutoPopulatingList<Matricula>(Matricula.class));
		matriculasAluno.getMatriculas().add(new Matricula());

		populateDefaultModel(model);
	}

	private void populateDefaultModel(Model model) {
		List<Curso> cursos = cursoService.getCursos();

		cursos.stream().forEach(curso -> cursosCache.put(curso.getCodigo(), curso));

		model.addAttribute("cursos", cursoService.getCursos());
	}
}
