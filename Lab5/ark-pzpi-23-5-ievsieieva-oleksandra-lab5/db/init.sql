--
-- PostgreSQL database dump
--

\restrict pwXoDSjc3htGMf8PIqeRWF05I4HmrzexRWqFsIiQfEyg6Uq6LY6OtlpAt3y3s3O

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: lamp; Type: TABLE; Schema: public; Owner: light
--

CREATE TABLE public.lamp (
    id integer CONSTRAINT "Lamp_lampId_not_null" NOT NULL,
    name character varying,
    "groupId" integer,
    description character varying,
    r smallint,
    g smallint,
    b smallint,
    brightness smallint,
    active boolean
);


ALTER TABLE public.lamp OWNER TO light;

--
-- Name: Lamp_lampId_seq; Type: SEQUENCE; Schema: public; Owner: light
--

ALTER TABLE public.lamp ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Lamp_lampId_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: event; Type: TABLE; Schema: public; Owner: light
--

CREATE TABLE public.event (
    id integer NOT NULL,
    lampid integer,
    groupid integer,
    r integer,
    g integer,
    b integer,
    brightness integer,
    active boolean,
    "timestamp" bigint
);


ALTER TABLE public.event OWNER TO light;

--
-- Name: event_id_seq; Type: SEQUENCE; Schema: public; Owner: light
--

ALTER TABLE public.event ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: group; Type: TABLE; Schema: public; Owner: light
--

CREATE TABLE public."group" (
    id integer NOT NULL,
    name character varying,
    description character varying
);


ALTER TABLE public."group" OWNER TO light;

--
-- Name: group_id_seq; Type: SEQUENCE; Schema: public; Owner: light
--

ALTER TABLE public."group" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: iot_stats; Type: TABLE; Schema: public; Owner: light
--

CREATE TABLE public.iot_stats (
    lamp_id integer,
    id integer NOT NULL,
    energy double precision,
    last_energy_update bigint,
    temperature double precision,
    uptime bigint,
    "timestamp" bigint
);


ALTER TABLE public.iot_stats OWNER TO light;

--
-- Name: iot_stats_id_seq; Type: SEQUENCE; Schema: public; Owner: light
--

ALTER TABLE public.iot_stats ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.iot_stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user; Type: TABLE; Schema: public; Owner: light
--

CREATE TABLE public."user" (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    salt character varying(255) NOT NULL,
    role character varying(20)
);


ALTER TABLE public."user" OWNER TO light;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: light
--

ALTER TABLE public."user" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: light
--

COPY public.event (id, lampid, groupid, r, g, b, brightness, active, "timestamp") FROM stdin;
1	17	\N	128	124	145	255	t	1765920457659
2	\N	\N	\N	\N	\N	\N	f	1765920565930
3	\N	\N	\N	\N	\N	\N	f	1765920590392
4	\N	\N	\N	\N	\N	\N	f	1765920614611
5	1	10	128	124	145	255	t	1765920692374
6	1	10	128	124	145	255	t	1765920721851
7	1	10	128	124	145	255	t	1766333298530
\.


--
-- Data for Name: group; Type: TABLE DATA; Schema: public; Owner: light
--

COPY public."group" (id, name, description) FROM stdin;
4	hall	The biggest room
5	hall	The biggest room
6	hall	The biggest room
8	hall	The biggest room
10	hall	The biggest room
12	hall	The biggest room
14	hall	The biggest room
16	hall	The biggest room
18	hall	The biggest room
20	hall	The biggest room
22	hall	The biggest room
24	hall	The biggest room
26	hall	The biggest room
28	hall	The biggest room
29	hall	The biggest room
30	hall	The biggest room
31	hall	The biggest room
32	hall	The biggest room
33	hall	The biggest room
34	hall	The biggest room
35	hall	The biggest room
36	hall	The biggest room
37	hall	The biggest room
38	hall	The biggest room
40	hall	The biggest room
42	hall	The biggest room
44	hall	The biggest room
46	hall	The biggest room
47	hall	The biggest room
\.


--
-- Data for Name: iot_stats; Type: TABLE DATA; Schema: public; Owner: light
--

COPY public.iot_stats (lamp_id, id, energy, last_energy_update, temperature, uptime, "timestamp") FROM stdin;
1	1	0.02033333294093609	4003	30.799999237060547	19053	1766340412381
1	2	1.276916742324829	19082	30.799999237060547	34106	1766340427415
1	3	2.5313334465026855	34135	30.799999237060547	49159	1766340442470
1	4	3.7858333587646484	49189	30.799999237060547	64213	1766340457522
1	5	5.040249824523926	64242	30.799999237060547	79266	1766340472572
1	6	6.294666290283203	79295	30.799999237060547	94319	1766340487624
1	7	7.5490827560424805	94348	30.799999237060547	109373	1766340502679
1	8	8.803749084472656	109404	30.799999237060547	124426	1766340517730
1	9	10.058082580566406	124456	30.799999237060547	139479	1766340532783
1	10	11.312499046325684	139509	30.799999237060547	154533	1766340547838
1	11	12.566915512084961	154562	31.799999237060547	169594	1766340562895
1	12	13.82208251953125	169624	31.799999237060547	184639	1766340577939
\.


--
-- Data for Name: lamp; Type: TABLE DATA; Schema: public; Owner: light
--

COPY public.lamp (id, name, "groupId", description, r, g, b, brightness, active) FROM stdin;
18	lamp 2	2	Main light in bedroom	\N	\N	\N	\N	f
19	lamp 2	2	Main light in bedroom	1	2	3	255	f
8	lamp 2	17	Main light in bedroom	\N	\N	\N	\N	f
4	lamp 2	9	Main light in bedroom	\N	\N	\N	\N	f
6	lamp 2	13	Main light in bedroom	\N	\N	\N	\N	f
3	lamp 1	2	Main light in hall	\N	\N	\N	\N	f
9	lamp 2	19	Main light in bedroom	\N	\N	\N	\N	f
7	lamp 2	15	Main light in bedroom	\N	\N	\N	\N	f
5	lamp 2	11	Main light in bedroom	\N	\N	\N	\N	f
17	\N	\N	\N	128	124	145	255	t
1	Analitic test lamp	10	\N	128	124	145	255	t
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: light
--

COPY public."user" (id, name, password, salt, role) FROM stdin;
2	user	885f22b53a9b2b0fd7fc51ce4ec46f00000b40adf37a14b660f7591a4739883a	97b15936eb73450eac118fee38112309ebacd7dcf326697fdbe982da0f39de9d	User
1	admin	ba4537d244002e0d82a7fbda80686b4aeacaf060a46f433313c8bd5f0eaf9804	317e32d0d1189eadd65fb7624f6e41b9331499c2373ae7f912a049b2aa6a57d1	Admin
\.


--
-- Name: Lamp_lampId_seq; Type: SEQUENCE SET; Schema: public; Owner: light
--

SELECT pg_catalog.setval('public."Lamp_lampId_seq"', 25, true);


--
-- Name: event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: light
--

SELECT pg_catalog.setval('public.event_id_seq', 7, true);


--
-- Name: group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: light
--

SELECT pg_catalog.setval('public.group_id_seq', 47, true);


--
-- Name: iot_stats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: light
--

SELECT pg_catalog.setval('public.iot_stats_id_seq', 12, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: light
--

SELECT pg_catalog.setval('public.user_id_seq', 2, true);


--
-- Name: lamp Lamp_pkey; Type: CONSTRAINT; Schema: public; Owner: light
--

ALTER TABLE ONLY public.lamp
    ADD CONSTRAINT "Lamp_pkey" PRIMARY KEY (id);


--
-- Name: group group_pkey; Type: CONSTRAINT; Schema: public; Owner: light
--

ALTER TABLE ONLY public."group"
    ADD CONSTRAINT group_pkey PRIMARY KEY (id);


--
-- PostgreSQL database dump complete
--

\unrestrict pwXoDSjc3htGMf8PIqeRWF05I4HmrzexRWqFsIiQfEyg6Uq6LY6OtlpAt3y3s3O

