# 🏥 MedFlow - Sistema de Gestão Hospitalar e Triagem

O **MedFlow** é um sistema desktop completo para gerenciamento de fluxo hospitalar, triagem de pacientes e alocação de leitos. O projeto simula o dia a dia real de uma unidade de pronto atendimento (UPA) ou hospital, desde a entrada do paciente na recepção até a alta médica, organizando o atendimento por gravidade com base no **Protocolo de Manchester**.

Este projeto foi desenvolvido focado em boas práticas de Programação Orientada a Objetos (POO), arquitetura em camadas e uma interface visual amigável para os operadores de saúde.

---

## 🚀 Funcionalidades do Sistema

* **Gestão de Recursos (RH & Estrutura):** Cadastro unificado de Profissionais de Saúde (Médicos e Enfermeiros) e controle do mapa físico de Leitos (UTI e Enfermaria).
* **Triagem Digital (Protocolo de Manchester):** Permite ao enfermeiro avaliar os sintomas e classificar os pacientes pelas cores de risco:
    * 🔴 **Vermelho:** Emergência (Atendimento Imediato)
    * 🟡 **Amarelo:** Urgência (Tempo de espera reduzido)
    * 🟢 **Verde:** Pouco Urgente (Fila padrão)
* **Fluxo de Atendimento e Alta:** Médicos visualizam a fila de prioridades, realizam o atendimento, mudam o status do paciente e gerenciam a ocupação ou liberação dos leitos.
* **Persistência In-Memory (Mock DB):** Simulação de um banco de dados relacional utilizando coleções dinâmicas do Java (`List`, `ArrayList`) com operações otimizadas via **Java Streams API**.

---

## 🖥️ Telas do Sistema (Interface Gráfica JFrame)

A interface gráfica foi totalmente construída utilizando **Java Swing**, dividida em módulos intuitivos:

1. **Painel de Recepção / Cadastro:** Tela para admissão de novos pacientes (gerados inicialmente com o status `AGUARDANDO_TRIAGEM`) e cadastro de novos profissionais.
2. **Módulo de Triagem (Visão do Enfermeiro):** Listagem de pacientes sem classificação. O enfermeiro seleciona o paciente, define a cor de gravidade e o encaminha para a fila médica.
3. **Consultório Clínico (Visão do Médico):** Painel que ordena os pacientes automaticamente pelo nível de urgência. O médico realiza a consulta, define se o paciente precisa de internação ou se receberá alta.
4. **Painel de Ocupação de Leitos:** Visualização em tempo real de quais leitos estão ocupados ou disponíveis no hospital.

---

## 📂 Arquitetura do Projeto

O sistema foi estruturado seguindo o padrão de **Separação de Responsabilidades**, garantindo um código limpo, testável e de fácil manutenção:

```text
src/
└── main/java/com.mycompany
    ├── model/         # Classes de Entidade (Paciente, Medico, Enfermeiro, Leito, Enums)
    ├── repository/    # Camada de Persistência (Simulação de Banco de Dados em Memória)
    ├── service/       # Camada de Negócio / "Cérebro" (Validações, regras de triagem e filas)
    ├── view/          # Camada Visual (Interfaces gráficas construídas com JFrame/Swing)
    └── Main.java      # Classe de entrada que inicializa a aplicação e a interface de usuário

```
🛠️ Tecnologias Utilizadas
Linguagem: Java 17+

Interface Gráfica: Java Swing / JFrame / Componentes visuais nativos

Recursos Avançados do Java: Java Streams API, Expressões Lambda, Polimorfismo e manipulação segura de coleções.

🧠 Conceitos de Programação Aplicados
Polimorfismo e Herança: Medico e Enfermeiro herdam da classe mãe ProfissionalSaude. Eles são armazenados em um único repositório genérico, mantendo seus atributos específicos (como a especialidade do médico) preservados na memória.

Java Streams & Lambdas: Utilizados para realizar buscas cirúrgicas por ID (.filter().findFirst().orElse(null)) e remoções ou atualizações seguras de registros em uma única linha de código.

Validação na Camada Service: O repositório atua de forma simples (CRUD), enquanto a camada Service protege a integridade do hospital, impedindo ações inválidas (como tentar remover um paciente inexistente ou internar em um leito ocupado).

⚙️ Como Executar o Projeto
Certifique-se de possuir o JDK 17 ou superior configurado em sua máquina.

Clone este repositório para o seu ambiente local:

Bash
   git clone [https://github.com/Luiz-Otavio-Rocha-Castro/MedFlow.git]
Abra a pasta do projeto em sua IDE favorita (IntelliJ IDEA, Eclipse ou VS Code).

Execute o arquivo src/Main/Main.java. A janela do sistema abrirá na sua tela automaticamente.

Desenvolvido com ☕ e focado na evolução de arquitetura de software por Luiz Otávio🩺