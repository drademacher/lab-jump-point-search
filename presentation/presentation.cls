% author:   D. Rademacher
% date:     25.04.2016
% version:  0.1
% about:    custom template for university presentations



% class configuration
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{presentation}[18/02/2016 v0.2 custom template for assignments]
\LoadClass[ignorenonframetext]{beamer}

% package for formatting section and subsection (cls internal)
% \RequirePackage{titlesec}



% list of necessary packages
\RequirePackage[utf8]{inputenc}
\RequirePackage[english]{babel}
\RequirePackage{amsfonts, amsmath, amssymb, amsthm}
\RequirePackage{nicefrac}
\RequirePackage[section]{placeins}

% list of optional packages
\RequirePackage{graphicx}
\RequirePackage{algpseudocode}
\RequirePackage{changepage}
\RequirePackage{xcolor,soul}
\RequirePackage{url}

% beamer packages
\RequirePackage{beamerthemesplit}
\RequirePackage{fancybox}
\RequirePackage{float}

% table spacing
\def\arraystretch{1.3}


% configuration of header and footer
\pagenumbering{arabic}


% big box command
\newcommand{\bigbox}[1]{
    \begin{beamercolorbox}[sep=16pt,center]{title}
        \usebeamerfont{title}
        #1
        \par
    \end{beamercolorbox}
}


% theme
\usetheme{default}
\usecolortheme{default}
\usefonttheme{professionalfonts}

\definecolor{Sapphire}{rgb}{0.03, 0.15, 0.4}

\setbeamercolor*{title}{fg=Sapphire, bg=gray!30}
\setbeamercolor*{frametitle}{fg=Sapphire, bg=gray!20}
\setbeamercolor*{structure}{fg=Sapphire, bg=gray!20}
\setbeamercolor*{palette primary}{use=structure, fg=Sapphire!70!black, bg=gray!30}
\setbeamercolor*{palette quaternary}{fg=Sapphire, bg=gray!20}
\setbeamercolor*{local structure}{fg=Sapphire!60}

\setbeamertemplate{mini frames}[box]
\setbeamertemplate{navigation symbols}{}
\setbeamertemplate{itemize items}[default]
\setbeamertemplate{enumerate items}[default]
\setbeamercovered{highly dynamic}


