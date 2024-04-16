--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

-- Started on 2024-04-16 23:38:44

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 25207)
-- Name: bookings; Type: TABLE; Schema: public; Owner: cinemauser
--

CREATE TABLE public.bookings (
    bookingid integer NOT NULL,
    userid integer,
    movieid integer,
    seatnumber integer,
    timing timestamp without time zone
);


ALTER TABLE public.bookings OWNER TO cinemauser;

--
-- TOC entry 220 (class 1259 OID 25227)
-- Name: bookings_bookingid_seq; Type: SEQUENCE; Schema: public; Owner: cinemauser
--

ALTER TABLE public.bookings ALTER COLUMN bookingid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.bookings_bookingid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 215 (class 1259 OID 25193)
-- Name: movies; Type: TABLE; Schema: public; Owner: cinemauser
--

CREATE TABLE public.movies (
    movieid integer NOT NULL,
    title text,
    leadactor text,
    image bytea,
    rating text
);


ALTER TABLE public.movies OWNER TO cinemauser;

--
-- TOC entry 218 (class 1259 OID 25212)
-- Name: shows; Type: TABLE; Schema: public; Owner: cinemauser
--

CREATE TABLE public.shows (
    showid integer NOT NULL,
    movieid integer,
    screenid integer,
    timing timestamp without time zone
);


ALTER TABLE public.shows OWNER TO cinemauser;

--
-- TOC entry 216 (class 1259 OID 25200)
-- Name: users; Type: TABLE; Schema: public; Owner: cinemauser
--

CREATE TABLE public.users (
    userid integer NOT NULL,
    emailadress text,
    password text
);


ALTER TABLE public.users OWNER TO cinemauser;

--
-- TOC entry 219 (class 1259 OID 25226)
-- Name: users_userid_seq; Type: SEQUENCE; Schema: public; Owner: cinemauser
--

ALTER TABLE public.users ALTER COLUMN userid ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_userid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 4854 (class 0 OID 25207)
-- Dependencies: 217
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: cinemauser
--

COPY public.bookings (bookingid, userid, movieid, seatnumber, timing) FROM stdin;
\.


--
-- TOC entry 4852 (class 0 OID 25193)
-- Dependencies: 215
-- Data for Name: movies; Type: TABLE DATA; Schema: public; Owner: cinemauser
--

COPY public.movies (movieid, title, leadactor, image, rating) FROM stdin;
\.


--
-- TOC entry 4855 (class 0 OID 25212)
-- Dependencies: 218
-- Data for Name: shows; Type: TABLE DATA; Schema: public; Owner: cinemauser
--

COPY public.shows (showid, movieid, screenid, timing) FROM stdin;
\.


--
-- TOC entry 4853 (class 0 OID 25200)
-- Dependencies: 216
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: cinemauser
--

COPY public.users (userid, emailadress, password) FROM stdin;
\.


--
-- TOC entry 4863 (class 0 OID 0)
-- Dependencies: 220
-- Name: bookings_bookingid_seq; Type: SEQUENCE SET; Schema: public; Owner: cinemauser
--

SELECT pg_catalog.setval('public.bookings_bookingid_seq', 11, true);


--
-- TOC entry 4864 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_userid_seq; Type: SEQUENCE SET; Schema: public; Owner: cinemauser
--

SELECT pg_catalog.setval('public.users_userid_seq', 2, true);


--
-- TOC entry 4706 (class 2606 OID 25211)
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: cinemauser
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (bookingid);


--
-- TOC entry 4702 (class 2606 OID 25199)
-- Name: movies movies_pkey; Type: CONSTRAINT; Schema: public; Owner: cinemauser
--

ALTER TABLE ONLY public.movies
    ADD CONSTRAINT movies_pkey PRIMARY KEY (movieid);


--
-- TOC entry 4708 (class 2606 OID 25216)
-- Name: shows shows_pkey; Type: CONSTRAINT; Schema: public; Owner: cinemauser
--

ALTER TABLE ONLY public.shows
    ADD CONSTRAINT shows_pkey PRIMARY KEY (showid);


--
-- TOC entry 4704 (class 2606 OID 25206)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: cinemauser
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (userid);


-- Completed on 2024-04-16 23:38:44

--
-- PostgreSQL database dump complete
--
