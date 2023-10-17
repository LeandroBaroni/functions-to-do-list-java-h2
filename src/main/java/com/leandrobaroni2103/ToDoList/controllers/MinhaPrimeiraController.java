package com.leandrobaroni2103.ToDoList.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
public class MinhaPrimeiraController {

  /*
   * GET - Buscar informação
   * POST - Adicionar um dado/informação
   * PUT - Alterar um dado/informação
   * DELETE - deletar informação
   * PATCH - Alterar somente uma parte da informação/dado
   *
   */

  @GetMapping("/primeiroMetodo")
  public String primeiraMesagem() {
    return "Funcionou";
  }
}
