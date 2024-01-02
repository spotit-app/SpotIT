function slugifyAuth0Id(auth0Id: string): string {
  return auth0Id.replace('|', '%7C');
}

export { slugifyAuth0Id };
