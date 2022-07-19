--
-- PostgreSQL database dump
--

-- Dumped from database version 14.4
-- Dumped by pg_dump version 14.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: persona; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.persona (id_persona, nome_persona, password, indirizzo, numero_telefono) FROM stdin;
1	edo	58	breddo	3333333
2	Massimo	74	Via cecco angiolieri 13	338298279
3	Laura	31	via gastone breddo 13	339456789
4	noemi	90	via prato 15	333456782
\.


--
-- Data for Name: impiegato; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.impiegato (id_impiegato, tipo, stipendio) FROM stdin;
1	Cassiere	2000
2	Libraio	3000
\.


--
-- Data for Name: cassiere; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cassiere (id_cassiere, numero_scrivania) FROM stdin;
1	0
\.


--
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (id_cliente) FROM stdin;
3
4
\.


--
-- Data for Name: libraio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libraio (id_libraio, numero_scrivania) FROM stdin;
2	0
\.


--
-- Data for Name: libro; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libro (id_libro, titolo, autore, genere, in_prestito) FROM stdin;
4	La coscienza di zeno	Italo Svevo	Romanzo	f
1	Divina Commedia	Dante Alighieri	Fantasy	f
2	Ulisse	James Joyce	Storico	f
3	Alla ricerca del tempo perduto	Marcel Proust	Romanzo	f
\.


--
-- Data for Name: libro_in_prestito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libro_in_prestito (id_libro, cliente) FROM stdin;
\.


--
-- Data for Name: libro_prenotato; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.libro_prenotato (id_prenotazione, libro, cliente, data_prenotazione) FROM stdin;
\.


--
-- Data for Name: prestito; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prestito (id_prestito, libro, prestante, ricevente, cliente, data_inizio_prestito, data_fine_prestito, multa_pagata) FROM stdin;
1	3	2	2	3	2022-07-08 14:46:57.932	2022-07-08 14:47:05.209	f
\.


--
-- PostgreSQL database dump complete
--

