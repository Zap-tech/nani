# Makefile for Development and Production Builds of Nani

.PHONY: repl fig-dev-ui watch-css
.PHONY: build build-ui build-server build-css
.PHONY: run
.PHONY: test deps check
.PHONY: clean clean-all
.PHONY: help


# Leiningen Batch file won't work on windows
# Need to call it from cmd.exe explicitly
UNAME := $(shell uname)
ifeq ($(UNAME), Linux)
LEIN_CMD = lein
else
ifeq ($(UNAME), Darwin)
LEIN_CMD = lein
else
LEIN_CMD = lein.bat
endif
endif


help:
	@echo "Nani Development and Production Build Makefile"
	@echo ""
	@echo "Development Commands:"
	@echo "  repl                       :: Start Clojure Development Repl"
	@echo "  fig-dev-ui                 :: Start and Watch Figwheel UI Build."
	@echo "  watch-css                  :: Start and Watch CSS Stylesheet Generation (Less)"
	@echo ""
	@echo "Production Commands:"
	@echo "  build                      :: Perform Production Build of Nani"
	@echo "  --"
	@echo "  build-ui                   :: Perform Production Build of UI Only"
	@echo "  build-server               :: Generates UberJar of Server"
	@echo "  build-css                  :: Builds CSS Stylesheets Once"
	@echo "  --"
	@echo "  run                        :: Run Production Server"
	@echo ""
	@echo "Testing Commands:"
	@echo "  test                       :: Run Server Tests"
	@echo ""
	@echo "Misc Commands:"
	@echo "  deps                       :: Pull and Install all dependencies"
	@echo "  check                      :: Checks the status of required pre-requisites"
	@echo "  clean                      :: Clean out build artifacts"
	@echo "  clean-all                  :: Clean out more build artifacts"
	@echo "  help                       :: Display this help message"


repl:
	$(LEIN_CMD) repl


fig-dev-ui:
	$(LEIN_CMD) figwheel


watch-css:
	less-watch-compiler resources/public/less resources/public/css main.less


build: build-css build-ui build-server


build-ui:
	$(LEIN_CMD) cljsbuild once prod


build-server:
	$(LEIN_CMD) uberjar


build-css:
	lessc resources/public/less/main.less resources/public/css/main.css


run:
	$(LEIN_CMD) run


test:
	$(LEIN_CMD) test


deps:
	$(LEIN_CMD) deps
	npm install less
	npm install less-watch-compiler


check:
	@sh ./scripts/check_prerequisites.sh


clean:
	$(LEIN_CMD) clean
	rm -f ./nani.db
	rm -rf ./resources/public/css


clean-all: clean
	rm -rf node_modules
