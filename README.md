<p align="center">
  <a href="" rel="noopener">
 <img src="https://github.com/openvalidation/openvalidation-ide/raw/readme/resources/openVALIDATION-IDE.png" alt="Project logo"></a>
</p>

<p align="center">
<a href="https://openvalidation-ide-ui.azurewebsites.net" target="_blank">Demo IDE</a> | <a href="https://docs.openvalidation.io/contribution/developer-guide/ide" target="_blank">Documentation</a>
<br/><br/>
  integrated development environment for <a href="https://github.com/openvalidation/openvalidation">openVALIDATION </a>
</p>

## introduction
openVALIDATION-IDE is a web-based development environment for users which aren't familiar with coding. Domain Experts, Business Analysts or Requirement Engineers can write complex validation rules themself using natural language. These validation rules will be translated into code through [openVALIDATION](https://github.com/openvalidation/openvalidation). 

Usually, an IDE is a very technical tool for coding. The openVALIDATION-IDE is an approach to hide all the technical features of an IDE and gives the user just the necessary tools for writing validation rules. It includes features like syntax highlighting, auto-completion or linting, but makes it look like a simple text-editor.

## run with docker
You can easily run the openVALIDATION-IDE on your local machine with docker, following these steps:
1. [Install docker](https://docs.docker.com/get-docker) if not already installed
2. Clone this repository with `git clone https://github.com/openvalidation/openvalidation-ide`
3. Navigate into the previously cloned repository with `cd openvalidation-ide`
4. Run `docker-compose up`
5. Open [localhost](http://localhost/) in your browser

## run local
