# Gerenciador de tarefas em Kotlin

Projeto desenvolvido como parte do primeiro módulo do curso de **Programação Android com Kotlin** da [Rocketseat](https://www.rocketseat.com.br/), com o objetivo de aplicar os conceitos fundamentais da linguagem Kotlin em um projeto prático de terminal.

---

## Sobre o projeto

O gerenciador de tarefas é uma aplicação de linha de comando. Nela o usuário pode adicionar, listar, buscar, atualizar, filtrar e excluir tarefas de forma interativa.

---

## Funcionalidades desenvolvidas

- Adicionar uma nova tarefa
- Listar todas as tarefas cadastradas
- Buscar tarefa por ID
- Alternar status da tarefa (concluída / não concluída)
- Excluir tarefa por ID
- Filtrar tarefas por status

---

## Conceitos que foram aplicados

- `data class` com `private constructor` e `companion object` para geração de IDs únicos via `AtomicInteger`
- Imutabilidade — atualização de objetos via criação de novas instâncias em vez de mutação direta
- Interface genérica com implementação em classe dedicada
- Funções de ordem superior: `.map`, `.filter`, `.forEach`, `.find`, `.removeIf`
- `joinToString` para formatação de listas
- Tratamento de nulos com `?.`, `?:` (Elvis operator), `isNullOrEmpty` e `let`
- Desestruturação de `data class` dentro de lambdas
- Funções reutilizáveis com responsabilidade única (ex: `formatTask`)

---

## Instruções para execução

1. Clone o repositório
2. Abra o projeto no **IntelliJ IDEA**
3. Execute o arquivo `DesafioTaskList.kt`
