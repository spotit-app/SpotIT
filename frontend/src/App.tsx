import { Route, Routes } from 'react-router-dom';
import { Loading, Navbar } from 'components';
import { Suspense } from 'react';
import {
  Home,
  ProtectedPage,
  PageNotFound,
  PersonalData,
  Courses,
  Education,
  Experience,
  ForeignLanguages,
  Interests,
  Other,
  Projects,
  Socials,
  SoftSkills,
  TechSkills,
  Profile
} from 'pages';

function App() {
  return (
    <>
      <Navbar />
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/profil"
            element={
              <ProtectedPage>
                <Profile />
              </ProtectedPage>
            }
          >
            <Route path="dane-osobowe" element={<PersonalData />} />
            <Route path="konta-spolecznosciowe" element={<Socials />} />
            <Route path="edukacja" element={<Education />} />
            <Route path="doswiadczenie" element={<Experience />} />
            <Route path="umiejetnosci-techniczne" element={<TechSkills />} />
            <Route path="umiejetnosci-miekkie" element={<SoftSkills />} />
            <Route path="jezyki-obce" element={<ForeignLanguages />} />
            <Route path="projekty" element={<Projects />} />
            <Route path="zainteresowania" element={<Interests />} />
            <Route path="kursy" element={<Courses />} />
            <Route path="inne" element={<Other />} />
          </Route>
          <Route path="*" element={<PageNotFound />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
