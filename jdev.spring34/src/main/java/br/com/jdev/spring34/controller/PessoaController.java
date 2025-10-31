package br.com.jdev.spring34.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.jdev.spring34.entity.Pessoa;
import br.com.jdev.spring34.entity.Telefone;
import br.com.jdev.spring34.repository.PessoaRepository;
import br.com.jdev.spring34.repository.TelefoneRepository;
import jakarta.validation.Valid;

@Controller
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	/**
	 * Método para ACESSAR pessoas
	 * 
	 * @return
	 */
	@GetMapping("/pessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		modelAndView.addObject("pessoas", pessoasIt);
		return modelAndView;
	}

	/**
	 * Método para SALVAR pessoa
	 * getDefaultMessage vem das anotações da entidade @NotBlank
	 * 
	 * @param pessoa
	 * @return
	 */
	@PostMapping(value = "/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
			modelAndView.addObject("pessoas", pessoasIt);
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage()); //Vem das anotações
			}
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
		
		pessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/pessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());

		return andView;
	}

	/**
	 * Método para LISTAR pessoas
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
	 * Método para EDITAR pessoas
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

	/**
	 * Método para EXCLUIR pessoa por ID
	 * 
	 * @param idpessoa
	 * @return
	 */
	@GetMapping("/excluir/{idpessoa}")
	public ModelAndView excluir(@PathVariable Long idpessoa) {

		pessoaRepository.deleteById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;

	}

	/**
	 * Método para PESQUISA pessoa por nome
	 * 
	 * @param nomepesquisa
	 * @return
	 */
	@PostMapping("/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/pessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}

	/**
	 * Método para ACESSAR telefones
	 * 
	 * @param idpessoa
	 * @return
	 */
	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable Long idpessoa) {

		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		return modelAndView;

	}

	/**
	 * Método para SALVAR telefone da pessoa
	 * 
	 * @param telefone
	 * @param pessoaid
	 * @return
	 */
	@PostMapping("/addfonePessoa/{pessoaid}")
	public ModelAndView addfonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
		
		if (telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty()) {
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
			List<String> msg = new ArrayList<String>();
			
			if (telefone.getNumero().isEmpty()) {
				msg.add("Número deve ser informado");
			}
			
			if (telefone.getTipo().isEmpty()) {
				msg.add("Tipo do número deve ser informado");
			}
			
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
			
		}
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		telefone.setPessoa(pessoa);
		
		telefoneRepository.save(telefone);
		
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		return modelAndView;
	}

	/**
	 * Metódo para EXCLUIR telefones
	 * 
	 * @param idtelefone
	 * @return
	 */
	@GetMapping("/excluirfone/{idtelefone}")
	public ModelAndView excluirfone(@PathVariable Long idtelefone) {

		Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

		telefoneRepository.deleteById(idtelefone);

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));

		return modelAndView;

	}
}
