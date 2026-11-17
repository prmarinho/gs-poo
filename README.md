# Territory Predictive Insights — Calor e Energia

**Global Solution 2º/2026 — Programação Orientada a Objetos (POO)**
**Tema:** CitizenTech · **Recorte escolhido:** 🌡️ Calor e energia — ondas de calor e risco de sobrecarga na rede elétrica

Sistema em Java (menu de texto) que lê sensores, prevê a próxima hora com duas estratégias simples e emite alertas explicados, para ajudar a responder:

> *"Qual problema vai atingir o nosso território nas próximas horas, e o que podemos fazer hoje para evitar ou reduzir o impacto?"*

---

## 👥 Integrantes e divisão de tarefas

| Integrante | RM | Responsabilidade |
|---|---|---|
| João Vitor Anunciação Oliveira | RM567539 | **Sensores, componentes e alertas:** `Sensor`, `SensorTemperatura`, `SensorCargaRede`, `SensorConsumoEnergia`, `ComponenteTerritorial`, `EstacaoDeColeta`, `PainelInformativo`, `NivelAlerta`, `Alerta`, `SistemaAlertas` |
| Paulo Ribeiro Marinho | RM567459 | **Previsões, dados do território e menu:** `EstrategiaPrevisao`, `PrevisaoMediaMovel`, `PrevisaoComparacao`, `DadosTerritorio`, `SistemaMonitoramento` |

Os dois revisaram o projeto inteiro, para que qualquer um consiga explicar qualquer parte do código.

---

## 🎯 Público beneficiado

**Quem sofre o problema:** os cerca de 18.500 moradores da *Região Leste — Setor 3* (território fictício), principalmente os **4.200 em grupo de risco ao calor**: idosos, crianças pequenas, gestantes e pessoas com doenças crônicas. Também sofrem os trabalhadores ao ar livre e os serviços que dependem de energia, como a UBS e a geladeira de vacinas.

**Quem usa o sistema:** a equipe da UBS, a associação de moradores e a coordenação local, que podem acionar a Defesa Civil e a distribuidora de energia.

**Qual decisão o sistema apoia:** *"Precisamos agir hoje?"* Por exemplo:
- abrir pontos de hidratação e resfriamento e avisar quem cuida de idosos antes do pico de calor;
- pedir à distribuidora para aliviar o transformador e pedir à comunidade que evite chuveiro elétrico e ferro de passar das 18h às 21h;
- divulgar no painel da UBS uma campanha de economia de energia no horário de pico.

Nenhum dado pessoal é usado: o território é descrito por **região, números totais e equipamentos**, nunca por pessoas.

---

## ▶️ Como compilar e executar

**Requisito:** JDK 11 ou superior.

Na pasta do projeto (a que tem o `README.md`), rode:

```bash
javac -encoding UTF-8 -d bin -sourcepath src src/br/com/territoryinsights/main/SistemaMonitoramento.java
java -cp bin br.com.territoryinsights.main.SistemaMonitoramento
```

- O primeiro comando compila todas as classes. O `-sourcepath src` faz o `javac` encontrar sozinho as classes do pacote `model`. Os `.class` vão para a pasta `bin`.
- O `-encoding UTF-8` faz o Java ler os acentos corretamente em qualquer sistema.
- Os comandos funcionam no Git Bash, no PowerShell, no Prompt de Comando, no Linux e no macOS.

**Eclipse:** *File → New → Java Project*, desmarque "Use default location" e escolha esta pasta. O Eclipse reconhece a pasta `src`. Depois rode `SistemaMonitoramento` com o botão Play.
**IntelliJ:** abra esta pasta, clique com o botão direito em `src` → *Mark Directory as → Sources Root* e rode `SistemaMonitoramento`.

**Código de acesso inicial:** `4321`. Ele é necessário para alterar os limites de alerta.

---

## 🧭 Roteiro rápido para testar

1. **Opção 2** logo no início: o sistema fica **em silêncio**, porque ainda não tem 5 leituras.
2. **Opção 6**: simula 24 horas. A cada hora aparecem as leituras e os alertas previstos para a hora seguinte (ATENÇÃO, ALERTA e CRÍTICO). No final, o alerta mais grave do dia é explicado e enviado ao painel.
3. **Opção 2**: mostra as duas previsões de cada sensor, com a explicação do cálculo, e os alertas com o motivo.
4. **Opção 3 → 3**, digitando `-5`: a população é **rejeitada** com mensagem e o valor anterior é mantido.
5. **Opção 3 → 5**, com código errado: **acesso negado**. Com o código `4321` e o limite de carga em `80`, rode a opção 4 com 13 horas e depois a opção 2: aparece um alerta **CRÍTICO**.
6. **Opção 7**: desligue o painel e rode a opção 2. O sistema avisa que **a mensagem não chegou à comunidade**.
7. **Opção 5**: status completo com território, componentes, sensores, estratégias e últimos alertas.

---

## 🧱 Estrutura e conceitos de POO

As classes seguem os nomes da estrutura obrigatória do enunciado e estão organizadas em pacotes, como nas aulas: `model` guarda as classes do domínio e `main` guarda a classe com o menu.

```
territory-predictive-insights/
├── README.md
├── .gitignore
└── src/br/com/territoryinsights/
    ├── main/
    │   └── SistemaMonitoramento.java    (classe principal com menu)
    └── model/
        ├── ComponenteTerritorial.java   (classe abstrata)
        ├── EstacaoDeColeta.java         (extends ComponenteTerritorial)
        ├── PainelInformativo.java       (extends ComponenteTerritorial)
        ├── Sensor.java                  (interface)
        ├── SensorTemperatura.java       (implements Sensor)
        ├── SensorCargaRede.java         (implements Sensor)
        ├── SensorConsumoEnergia.java    (implements Sensor)
        ├── DadosTerritorio.java         (classe com encapsulamento)
        ├── EstrategiaPrevisao.java      (classe abstrata)
        ├── PrevisaoMediaMovel.java      (extends EstrategiaPrevisao)
        ├── PrevisaoComparacao.java      (extends EstrategiaPrevisao)
        ├── NivelAlerta.java             (enum: NORMAL, ATENÇÃO, ALERTA, CRÍTICO)
        ├── Alerta.java                  (um alerta e o motivo dele)
        └── SistemaAlertas.java          (classifica e guarda os alertas)
```

| Critério do enunciado | Onde está no código |
|---|---|
| **Classe abstrata** `ComponenteTerritorial` com id, nome, região e status | métodos concretos `ativar()` e `desativar()`; método abstrato `verificarStatus()` |
| Classe concreta que herda dela (`extends`) | `EstacaoDeColeta` e `PainelInformativo`, cada uma com o seu `verificarStatus()` |
| **Interface** `Sensor` (ler valor, verificar funcionamento, retornar tipo) | `lerValor()`, `verificarFuncionamento()`, `getTipo()`, `setLimiteAlerta()`, `passouDoLimite()`, entre outros, mais as constantes `CHANCE_DE_FALHA`, `VALOR_DE_FALHA` e `MAXIMO_HISTORICO` |
| 3 classes com `implements Sensor` | `SensorTemperatura` (°C), `SensorCargaRede` (% da capacidade do transformador) e `SensorConsumoEnergia` (MWh) |
| **Encapsulamento** em `DadosTerritorio` | todos os atributos `private`; o construtor usa os setters; os setters validam e, se o valor for inválido, mostram uma mensagem, retornam `false` e não alteram nada; o código de acesso **não tem getter** e é pedido para alterar os limites |
| **Herança** `EstrategiaPrevisao` + 2 tipos | `PrevisaoMediaMovel` (atributo `tamanhoJanela`) e `PrevisaoComparacao` (atributo `tamanhoPeriodo`); as duas sobrescrevem `prever()` e usam `setExplicacao()` da mãe, porque os atributos da mãe são `private` |
| Uso de `super` | `super(...)` nos construtores das 4 subclasses e `super.descrever()` nas previsões |
| Leituras em `ArrayList` | cada sensor guarda as últimas 48 leituras (`MAXIMO_HISTORICO`) em um `ArrayList<Double>` |
| **Associação** | a estação tem `sensoresInstalados`, e cada `Alerta` guarda o `sensorMonitorado` que o gerou |
| **Alertas** automáticos com níveis e motivo | `SistemaAlertas.avaliar()` e `classificar()`; `Alerta.exibir()` mostra sensor, valor previsto, limite, quanto passou e o que fazer |
| **Bônus**: simulação de 24 horas | opção 6 do menu (`simular24Horas()`) |

**Polimorfismo:** o menu percorre listas de `ComponenteTerritorial`, `Sensor` e `EstrategiaPrevisao` e chama o mesmo método em todos os objetos. Cada um responde do seu jeito, sem nenhum `if` para descobrir o tipo.

---

## 🧠 Decisões da equipe

- **Recorte calor e energia:** ondas de calor afetam a saúde e a rede elétrica ao mesmo tempo. O calor aumenta o uso de ar-condicionado e ventiladores, o que pode sobrecarregar o transformador justo quando a UBS mais precisa de energia.
- **Três sensores que se completam:** temperatura (risco à saúde), carga do transformador (risco de queda de energia) e consumo da região (risco de faltar energia no horário de pico).
- **Simulação:** cada sensor tem um vetor com o valor típico de cada hora de um dia quente (`perfilHorario`), e somamos uma variação com `Math.random()`. Assim os valores sobem e descem ao longo do dia como na realidade.
- **Cada leitura vale 1 hora** e as previsões são sempre para a **próxima hora**. A estação de coleta controla o horário.
- **Falhas:** 2% das leituras simulam defeito no sensor (`CHANCE_DE_FALHA`). Essa leitura é descartada e não entra no histórico nem nas previsões.
- **Duas estratégias simples:**
  - *Média Móvel*: média das últimas 5 leituras. É estável, mas demora a reagir a mudanças.
  - *Comparação de períodos*: compara a média das últimas 3 leituras com a das 3 anteriores. A diferença dividida por 3 é a tendência por hora, e a previsão é a última leitura mais a tendência. Reage mais rápido, mas erra quando a curva muda de direção (depois do pico).
- **Níveis de alerta em relação ao limite:** ATENÇÃO a partir de 90% (`FATOR_ATENCAO`); ALERTA acima de 100%; CRÍTICO a partir de 110% (`FATOR_CRITICO`). Vale o nível mais grave entre as duas estratégias, e o alerta mostra quantas estratégias indicam risco.
- **Limites:** ficam guardados em `DadosTerritorio`, protegidos pelo código. Depois de uma alteração autorizada, o menu atualiza os sensores com o novo valor.
- **Padrões das aulas:** atributos sempre `private`, sem números soltos no código (constantes com nome em maiúsculas), métodos com nome de verbo, variáveis de associação com o nome do papel (`sensorMonitorado`, `sensoresInstalados`) e JavaDoc explicando a interface e os métodos abstratos.

---

## 🔍 Reflexão crítica

**Quando o sistema deve se calar?** Com menos de 5 leituras, qualquer média ou tendência é chute. Por isso o sistema mostra *"sem previsão: tem X leituras e precisa de 5"* em vez de inventar um número, e a Comparação de períodos só funciona com 6 leituras (dois períodos completos). Leituras com defeito também são descartadas, porque um valor errado puxaria a média e poderia esconder um risco real. Se a estação estiver desligada, o sistema avisa que está usando dados antigos. Há ainda casos em que o sistema deveria falar com mais cuidado: quando as estratégias não concordam, o alerta avisa que *"vale confirmar com as próximas leituras"*, e logo depois do pico a Comparação de períodos ainda prevê uma subida que não vai acontecer.

**O que os dados não representam?** Uma única estação mede a temperatura de **um ponto** para 18.500 pessoas. Ela não mostra a casa sem ventilação embaixo de uma laje, nem a rua sem árvores que é bem mais quente. A carga de um transformador não representa toda a rede. Os dados são simulados a partir de um dia quente típico: não vêm de uma estação meteorológica real e não sabem se amanhã vai chover. O sistema também não sabe *quem* não tem ventilador ou depende de um aparelho elétrico de saúde. Isso foi de propósito, para não coletar dados pessoais, mas significa que o sistema aponta **horários e riscos**, não pessoas.

**Por que o alerta apoia a decisão, em vez de decidir?** Porque o sistema não conhece o contexto: um evento no bairro, uma obra da distribuidora, um feriado, um ponto de resfriamento que já está aberto. Um alarme falso faz a comunidade parar de confiar nos avisos, e um alerta perdido pode custar a saúde de alguém. Quem pesa esse equilíbrio é a pessoa responsável pelo território. Por isso cada alerta mostra **o motivo** (sensor, valor previsto, limite, quanto passou e quantas estratégias concordam), para que a pessoa possa conferir e discordar. As ações são só sugestões, os limites são definidos por pessoas autorizadas (com código de acesso), e o próprio alerta lembra: *"o sistema só sugere, quem decide é a equipe responsável pelo território"*.

---

## 🤖 Uso de IA

Usamos o Claude (Anthropic), pelo Claude Code, como apoio no desenvolvimento. A partir das decisões da equipe (recorte, bônus, divisão de tarefas e padrões das aulas), ele deu o norte de como iniciar, documentação e ao final testou o programa. Também usamos a ferramenta para tirar dúvidas (por exemplo, sobre JavaDoc e a estrutura do projeto no IntelliJ) e receber sugestões para alinhar o código ao conteúdo das aulas. Cada integrante é responsável por explicar a parte do código indicada na divisão de tarefas.

---

## 🚧 Limitações e próximos passos

- Usar **dados reais do INMET** (temperatura por hora) como base da simulação.
- **Salvar o histórico em arquivo** (CSV) e carregar ao abrir o programa.
- Ter **mais de uma estação por região**, para o dado de um ponto não representar o território inteiro.
- Não deixar o código de acesso escrito no código-fonte e esconder a digitação dele no console.
- Aceitar sensores em que o risco é ficar **abaixo** do limite (ex.: umidade do ar muito baixa). Hoje todos os sensores consideram risco o valor **acima** do limite.
