package br.com.jdev.spring34.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String inicio() {
		return "cadastro/pessoa";
	}

	/**
	 * Método para salvar pessoa
	 * @param pessoa
	 * @return
	 */
	@PostMapping(value = "/salvarpessoa")
	public String salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		return "cadastro/pessoa";
	}
	
	/**
	 * Método para listar pessoas
	 * @return
	 */
	@GetMapping("/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/pessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		return andView;
	}
}











