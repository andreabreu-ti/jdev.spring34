package br.com.jdev.spring34.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.jdev.spring34.entity.Pessoa;
import br.com.jdev.spring34.repository.PessoaRepository;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	/**
	 * Método de redirecionamento
	 * 
	 * @return
	 */
	@GetMapping("/pessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}

	/**
	 * Método para salvar pessoa
	 * 
	 * @param pessoa
	 * @return
	 */
	@PostMapping(value = "*/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/pessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;
	}

	/**
	 * Método para listar pessoas
	 * 
	 * @return
	 */
	@GetMapping("/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/pessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		return andView;
	}

	/**
	 * 
	 * @param idpessoa
	 * @return
	 */
	@GetMapping("/editar/{idpessoa}")
	public ModelAndView editar(@PathVariable Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoaobj", pessoa.get());
		return modelAndView;

	}

}
